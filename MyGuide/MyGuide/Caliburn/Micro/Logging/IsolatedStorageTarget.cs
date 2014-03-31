using NLog;
using NLog.Common;
using NLog.Config;
using NLog.Targets;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Caliburn.Micro.Logging
{
    [Target("ISFile")]
    public sealed class IsolatedStorageTarget : TargetWithLayout
    {
        private const int OPEN_FILE_TIMEOUT = 60;
        private const int SIZE_LIMIT = 200 * 1024; // 200 KB in bytes
        private Timer _autoClosingTimer;
        private IsolatedStorageFileStream _fileStream;
        private TextWriter _fileWriter;
        private bool _isInitialized = false;
        private IsolatedStorageFile _isolatedStorage;

        public IsolatedStorageTarget()
        {
            AutoClose = true;
            AutoFlush = false;
            FileSizeLimit = SIZE_LIMIT;
            OpenFileTimeout = OPEN_FILE_TIMEOUT;
        }

        [DefaultValue(true)]
        public bool AutoClose { get; set; }

        [DefaultValue(false)]
        public bool AutoFlush { get; set; }

        [RequiredParameter]
        public string FileName { get; set; }

        [DefaultValue(SIZE_LIMIT)]
        public long FileSizeLimit { get; set; }

        [DefaultValue(OPEN_FILE_TIMEOUT)]
        public int OpenFileTimeout { get; set; }

        protected override void CloseTarget()
        {
            if (_autoClosingTimer != null)
            {
                _autoClosingTimer.Change(Timeout.Infinite, Timeout.Infinite);
                _autoClosingTimer.Dispose();
                _autoClosingTimer = null;
            }

            if (_fileWriter != null)
            {
                _fileWriter.Dispose();
                _fileWriter = null;
            }

            if (_fileStream != null)
            {
                _fileStream.Dispose();
                _fileStream = null;
            }

            if (_isolatedStorage != null)
            {
                _isolatedStorage.Dispose();
                _isolatedStorage = null;
            }

            _isInitialized = false;
        }

        protected override void FlushAsync(AsyncContinuation asyncContinuation)
        {
            try
            {
                _fileWriter.Flush();
                asyncContinuation(null);
            }
            catch (Exception exception)
            {
                asyncContinuation(exception);
            }
        }

        protected override void InitializeTarget()
        {
            base.InitializeTarget();

            InitializeISFile();
        }

        protected override void Write(LogEventInfo logEvent)
        {
            try
            {
                if (!_isInitialized)
                {
                    InitializeISFile();
                }
                WriteToIsolatedStorage(this.Layout.Render(logEvent));
            }
            catch (Exception e)
            {
                InternalLogger.Warn("Exception in IsolatedStorageTarget Write: {0}", e);
                throw;
            }
        }

        private void AutoClosingTimerCallback(object state)
        {
            if (_isInitialized)
            {
                CloseTarget();
            }
        }

        private void InitializeISFile()
        {
            if (!_isInitialized)
            {
                _isolatedStorage = IsolatedStorageFile.GetUserStoreForApplication();
                _fileStream = new IsolatedStorageFileStream(FileName, FileMode.Append, FileAccess.Write, FileShare.ReadWrite, _isolatedStorage);
                _fileWriter = new StreamWriter(_fileStream);
                SetClosingTimer();
                _isInitialized = true;
            }
        }

        private void SetClosingTimer()
        {
            if (OpenFileTimeout > 0 && AutoClose)
            {
                _autoClosingTimer = new Timer(
                    AutoClosingTimerCallback,
                    null,
                    OpenFileTimeout * 1000,
                    OpenFileTimeout * 1000);
            }
        }

        private void WriteToIsolatedStorage(string msg)
        {
            // If the log file is too big we clear it and create new one...
            if (_fileStream.Length > FileSizeLimit)
            {
                _fileWriter.Dispose();
                _fileStream.Dispose();
                _fileStream = new IsolatedStorageFileStream(FileName, FileMode.Truncate, FileAccess.Write, FileShare.ReadWrite, _isolatedStorage);
                _fileWriter = new StreamWriter(_fileStream);
            }

            _fileWriter.WriteLine(msg);
            if (AutoFlush)
            {
                FlushAsync(e =>
                {
                    if (e != null)
                    {
                        InternalLogger.Warn("Exception in WriteToIsolatedStorage: {0}", e);
                    }
                });
            }
        }
    }
}