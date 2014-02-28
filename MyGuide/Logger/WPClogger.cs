using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.IO.IsolatedStorage;
using System.Text;
using System.Xml;
using System.Xml.Linq;
using Microsoft.Phone.Tasks;

/*
 * Change the namespace to your app's namespace!
 */
namespace Logger
{
    /// <summary>
    /// Custom LogLevel enum accessible from the rest of the app
    /// </summary>
    public enum LogLevel { debug, info, warn, error, critical }
    /// <summary>
    /// WPClogger v0.3
    /// Project site: www.wpclogger.codeplex.com
    /// </summary>
    public class WPClogger
    {
        #region logging variables
        private static String[] levelLabel = new String[] { "DEBUG", "INFO", "WARN", "ERROR", "CRITICAL" };
        private List<WPCException> buffer = new List<WPCException>();
        IDictionary<string, string> dictionary = new Dictionary<string,string>();
        private IsolatedStorageFile iso = IsolatedStorageFile.GetUserStoreForApplication();
        private IsolatedStorageSettings settings = IsolatedStorageSettings.ApplicationSettings;
        private bool toFile = false;
        private const String fileName = "WPCLog.xml";
        private LogLevel loggerLevel = LogLevel.warn;
        private XDocument loadedDoc = null;
        #endregion
        #region configurable variables
        private const String emailAddress = "app@wpcentral.com";
        private const String emailDefaultUserComments = "<Add any extra detail you'd like to here, or you can hit send now, the technical information below will help>";
        private const String emailSubject = "WPClogger Bug Report: WPCentral for Windows Phone 8";
        private long maxFileSize = 38000;
        #endregion

        #region email reports
        /// <summary>
        /// Generate an e-mail task with the log file content to submit to a support address
        /// </summary>
        public void emailReport()
        {
            emailReport(null, null);
        }


        /// <summary>
        /// Generate an e-mail task with the log file content to submit to a support address
        /// </summary>
        /// <param name="userComments">A string of user comments if you wish to take an input from a textbox etc</param>
        public void emailReport(String userComments)
        {
            emailReport(userComments, null);
        }

        /// <summary>
        /// Generate an e-mail task with the log file content to submit to a support address
        /// </summary>
        /// <param name="userComments">A string of user comments if you wish to take an input from a textbox etc</param>
        /// <param name="subject">A custom e-mail subject string</param>
        public void emailReport(String userComments, String subject)
        {
            List<WPCException> exceptions = readFiletoMemory();
            EmailComposeTask emailTask = new EmailComposeTask();
            StringBuilder builder = new StringBuilder();
            if (subject == null)
            {
                emailTask.Subject = emailSubject;
            }
            else
            {
                emailTask.Subject = subject;
            }
            emailTask.To = emailAddress;
            builder.AppendLine("User Comments:");
            if (userComments == null || userComments.Equals(""))
            {
                builder.AppendLine(emailDefaultUserComments);
            }
            else
            {
                builder.AppendLine(userComments);
            }
            builder.AppendLine(getParamsDictionary());
            builder.AppendLine("=Auto Generated Technical Detail=");
            foreach (WPCException ex in exceptions)
            {
                builder.AppendLine(ex.getEmailOutput());
            }
            builder.AppendLine("\n\nBug reporting provided by the WPClogger");
            emailTask.Body = builder.ToString();
            try
            {
                emailTask.Show();
            }
#pragma warning disable
            catch (ArgumentOutOfRangeException e)
#pragma warning restore
            {
                if (emailTask.Body.Length > 30000)
                {
                    emailTask.Body = emailTask.Body.Substring(0, 30000);
                }
                else
                {
                    emailTask.Body = emailTask.Body.Substring(0, emailTask.Body.Length/2);
                }
                emailTask.Body += "\nTruncated due to email size limitation";
                emailTask.Show();
            }
        }
        #endregion

        #region constructors
        /// <summary>
        /// Default constructor only logging critical exceptions
        /// </summary>
        public WPClogger()
        {
            this.loggerLevel = LogLevel.critical;
        }

        /// <summary>
        /// Custom constructor logging up to a specified logging level
        /// </summary>
        /// <param name="level">The LogLevel to which this logger will start logging to</param>
        public WPClogger(LogLevel level)
        {
            this.loggerLevel = level;
        }
        #endregion

        #region levelSetters
        public void setLevel(String level)
        {
            this.loggerLevel = getLevel(level);
        }

        public void setLevel(LogLevel level)
        {
            this.loggerLevel = level;
        }
        #endregion

        #region loggers
        /// <summary>
        /// Log a simple debug message (replaces Debug.WriteLine())
        /// </summary>
        /// <param name="message">The message to log</param>
        public void log(String message)
        {
            Exception e = new Exception(message);
            log(LogLevel.debug, e);
        }

        /// <summary>
        /// Log a message with any logging level
        /// </summary>
        /// <param name="level">The LogLevel to assign to this message</param>
        /// <param name="message">The message to log</param>
        public void log(LogLevel level, String message)
        {
            Exception e = new Exception(message);
            log(level, e);
        }

        /// <summary>
        /// Log a message with custom additional information (for instance variable values at the time of the exception)
        /// </summary>
        /// <param name="level">The LogLevel to assign to this message</param>
        /// <param name="message">The message to log</param>
        /// <param name="comment">Any additional information</param>
        public void log(LogLevel level, String message, String comment)
        {
            Exception e = new Exception(message);
            log(level, e, comment);
        }

        /// <summary>
        /// Log an exception object at level ERROR, example use in a catch block
        /// </summary>
        /// <param name="e">The exception object to log</param>
        public void log(Exception e)
        {
            log(LogLevel.error, e);
        }

        /// <summary>
        /// Log an exception object at a specificed level, example use in a catch block
        /// </summary>
        /// <param name="level">The LogLevel to assign to this message</param>
        /// <param name="e">The exception object to log</param>
        public void log(LogLevel level, Exception e)
        {
            log(level, e, null);
        }

        /// <summary>
        /// Log an exception object with custom additional information (for instance variable values at the time of the exception), example use in a catch block
        /// </summary>
        /// <param name="level">The LogLevel to assign to this message</param>
        /// <param name="e">The exception object to log</param>
        /// <param name="comment">Any additional information</param>
        public void log(LogLevel level, Exception e, String comment)
        {
            if ((int)loggerLevel <= (int)level)
            {
                WPCException ex = new WPCException(level, e, comment);
                if (Debugger.IsAttached)
                {
                    Debug.WriteLine(ex.getDebugOutput());
                }
                if (toFile || level.Equals(LogLevel.critical))
                {
                    logToFile(ex);
                }
            }
        }

        #endregion

        #region fileLogging

        /// <summary>
        /// Turn on/off file logging for levels below CRITICAL, use to gather additional information about certain work logs, then disable again
        /// </summary>
        /// <param name="value">True: enable, False: disable</param>
        public void setFileRecording(bool value)
        {
            this.toFile = value;
        }

        /// <summary>
        /// Testable boolean to identify if the logger is currently writing to file
        /// </summary>
        /// <returns>True if currently logging to file, False if not</returns>
        public Boolean isLoggingToFile()
        {
            return this.toFile;
        }

        /// <summary>
        /// Wrapper for the actual file logging procedure, if a critical caused this error we can't thread the file writing otherwise the application could exit
        /// </summary>
        /// <param name="ex">The exception to log</param>
        private void logToFile(WPCException ex)
        {
            if (!ex.isCritical())
            {
                BackgroundWorker thread = new BackgroundWorker();
                thread.DoWork += ((obj, e) =>
                {
                    actualLogToFile(ex);
                });
                thread.RunWorkerAsync();
            }
            else
            {
                actualLogToFile(ex);
            }
        }

        /// <summary>
        /// The actual logging to file procedure, with threading decision handled in the wrapper
        /// </summary>
        /// <param name="ex">The exception to log</param>
        private void actualLogToFile(WPCException ex)
        {
            try
            {
                if (!loadDocument())
                {
                    createDocument();
                }
                if (buffer.Count > 0)
                {
                    foreach (WPCException bufferedEx in buffer)
                    {
                        this.loadedDoc.Element("WPCLog").Add(bufferedEx.convertToElement());
                    }
                    buffer.Clear();
                }
                this.loadedDoc.Element("WPCLog").Add(ex.convertToElement());
                if (ex.isCritical())
                {
                    refreshParameterXML();
                    saveLoadedDoc();
                }
            }
#pragma warning disable
            catch (IsolatedStorageException isoEx)
#pragma warning restore
            {
                //Some apps will use the isolated storage on startup, preventing logging, so we write the event to a buffer for next time
                buffer.Add(ex);
            }
        }

        /// <summary>
        /// Completely purges the log of all content (including stored paramters)
        /// </summary>
        public void purgeLog()
        {
            BackgroundWorker thread = new BackgroundWorker();
            thread.DoWork += ((obj, e) =>
            {
                this.loadedDoc = null;
                if (iso.FileExists(fileName))
                {
                    iso.DeleteFile(fileName);
                }
            });
            thread.RunWorkerAsync();
        }

        /// <summary>
        /// Clears out all exceptions, but leaves the parameters intact
        /// </summary>
        public void clearEventsFromLog()
        {
            BackgroundWorker thread = new BackgroundWorker();
            thread.DoWork += ((obj, e) =>
            {
                this.loadedDoc = null;
                createDocument();
                saveLoadedDoc();
            });
            thread.RunWorkerAsync();
        }

        /// <summary>
        /// Test for critical errors that have been logged, use on app startup to determine if a crash previously occurred
        /// </summary>
        /// <returns>True if criticals are logged, False if not</returns>
        public bool hasCriticalLogged()
        {
            if (loadDocument())
            {
                IEnumerable<XElement> elements = loadedDoc.Descendants("Event");
                foreach (XElement el in elements)
                {
                    if (el.Attribute("level").Value.Equals(levelLabel[(int)LogLevel.critical]))
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        private Boolean loadDocument()
        {
            if (this.loadedDoc == null && iso.FileExists(fileName))
            {
                try
                {
                    IsolatedStorageFileStream file = new IsolatedStorageFileStream(fileName, System.IO.FileMode.Open, iso);
                    try
                    {
                        if (file.Length > maxFileSize)
                        {
                            file.Close();
                            iso.DeleteFile(fileName);
                            buffer.Add(new WPCException(LogLevel.info,new Exception("Log reached maximum size, purging")));
                            return false;
                        }
                        loadedDoc = XDocument.Load(file);
                        loadParameters();
                        file.Close();
                        return true;
                    }
                    catch (System.Xml.XmlException exception)
                    {
                        file.Close();
                        log(LogLevel.error, exception);
                        return false;
                    }
                }
                catch (IsolatedStorageException ex)
                {
                    throw ex;
                }
            }
            else if (loadedDoc != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        private void createDocument()
        {
            this.loadedDoc = new XDocument();
            XElement root = new XElement("WPCLog");
            root.Add(getParamsDictionaryXML());
            this.loadedDoc.Add(root);
        }

        private List<WPCException> readFiletoMemory()
        {
            List<WPCException> toReturn = new List<WPCException>();
            if (loadDocument())
            {
                IEnumerable<XElement> elements = loadedDoc.Descendants("Event");
                foreach (XElement el in elements)
                {
                    toReturn.Add(new WPCException(el));
                }
            }
            return toReturn;
        }

        /// <summary>
        /// Testable condition to identify if the logger has a file open
        /// </summary>
        /// <returns>True if a log is currently present, False if not</returns>
        public Boolean hasLogFile()
        {
            return (loadedDoc != null || iso.FileExists(fileName));
        }

        /*
         * Public wrapper to be called on exit 
         */
        public void saveLog()
        {
            refreshParameterXML();
            saveLoadedDoc();
        }

        private void saveLoadedDoc()
        {
            if (this.loadedDoc != null)
            {
                try
                {
                    IsolatedStorageFileStream file = new IsolatedStorageFileStream(fileName, System.IO.FileMode.Create, iso);
                    this.loadedDoc.Save(file);
                    file.Close();
                }
                catch (System.IO.IsolatedStorage.IsolatedStorageException IOex)
                {
                    log(LogLevel.error, IOex);
                }
            }
        }

        #endregion

        #region parameters Dictionary

        private String getParamsDictionary()
        {
            StringBuilder builder = new StringBuilder();
            builder.AppendLine("\n=Parameter Dictionary=");
            foreach (string key in dictionary.Keys)
            {
                builder.Append(key);
                builder.Append(" : ");
                string value = null;
                dictionary.TryGetValue(key, out value);
                if (value != null)
                {
                    builder.AppendLine(value);
                }
                else
                {
                    builder.AppendLine("null");
                }
            }
            return builder.ToString();
        }

        private XElement getParamsDictionaryXML()
        {
            XElement root = new XElement("Parameters");
            foreach (string key in dictionary.Keys)
            {
                try
                {
                    XElement container = new XElement(key);
                    string value = null;
                    dictionary.TryGetValue(key, out value);
                    if (value != null)
                    {
                        container.Value = value;
                    }
                    else
                    {
                        container.Value = "null";
                    }
                    root.Add(container);
                }
                catch (XmlException e)
                {
                    log(LogLevel.warn, e, "Errored converting parameter key: " + key);
                }
            }
            return root;
        }
        /// <summary>
        /// Sets a permanent parameter (such as the App's version or user preferences) which will be included with bug reports.
        /// Note this will overwrite any key of the same name
        /// </summary>
        /// <param name="key">The parameter name & unique key</param>
        /// <param name="value">Value to assign to the parameter</param>
        public void setParameter(string key, string value)
        {
            if (this.dictionary.ContainsKey(key))
            {
                this.dictionary.Remove(key);
            }
            this.dictionary.Add(key, value);
        }

        /// <summary>
        /// Un-set a parameter from the dictionary
        /// </summary>
        /// <param name="key">The parameter name & unique key</param>
        public void unsetParameter(string key)
        {
            if (this.dictionary.ContainsKey(key))
            {
                this.dictionary.Remove(key);
            }
        }

        /// <summary>
        /// Clear out the parameters dictionary
        /// </summary>
        public void clearParameters()
        {
            this.dictionary.Clear();
        }

        private void loadParameters()
        {
            XElement param = this.loadedDoc.Element("Parameters");
            if (param != null)
            {
                IEnumerable<XElement> elements = param.Elements();
                foreach (XElement el in elements)
                {
                    //If we're loading params from a saved file and it already exists, chances are the file is old
                    if (!this.dictionary.ContainsKey(el.Name.ToString()))
                    {
                        this.dictionary.Add(el.Name.ToString(), el.Value);
                    }
                }
            }
        }

        private void refreshParameterXML()
        {
            if (loadDocument())
            {
                XElement param = loadedDoc.Element("Parameters");
                if (param != null)
                {
                    param.Remove();
                }
                param = getParamsDictionaryXML();
                loadedDoc.Root.Add(param);
            }
            else
            {
                log(LogLevel.error, "Failed loading the XML document when writing parameters");
            }
        }

        /// <summary>
        /// Collect any objects stored in the IsolatedUserStore and add them to the params dictionary
        /// Warning: some objects may have long string representations
        /// </summary>
        public void convertUserStoreToParams()
        {
            foreach (String key in settings.Keys)
            {
                try
                {
                    setParameter(key, settings[key].ToString());
                }
#pragma warning disable
                catch (Exception e)
#pragma warning restore
                {
                    //Catch all exceptions (and do not log) in case ToString throws an exception
                }
            }
        }

        /// <summary>
        /// Remove all objects stored in the IsolatedUserStore from the dictionary
        /// Recommended if you wish to keep memory usage down after submitting a bug report or similar
        /// </summary>
        public void removeUserStoreFromParams()
        {
            foreach (String key in settings.Keys)
            {
                unsetParameter(key);
            }
        }

        #endregion

        #region levelLogic

        private static LogLevel getLevel(String level)
        {
            level = level.ToUpper();
            for (int i = 0; i < levelLabel.Length; i++)
            {
                if (levelLabel[i].Equals(level))
                {
                    return (LogLevel)i;
                }
            }
            return 0;
        }

        #endregion

        #region WPCException
        internal class WPCException
        {
            private int eLevel;
            private String message;
            private String stackTrace = null;
            private String comment = null;
            private DateTime timestamp = DateTime.Now;

            public WPCException(Exception e)
            {
                this.message = e.Message;
                this.stackTrace = e.StackTrace;
                this.eLevel = (int)LogLevel.error;
            }

            public WPCException(LogLevel level, Exception e)
            {
                this.message = e.Message;
                this.stackTrace = e.StackTrace;
                this.eLevel = (int)level;
            }

            public WPCException(LogLevel level, Exception e, String comment)
            {
                this.message = e.Message;
                this.stackTrace = e.StackTrace;
                this.eLevel = (int)level;
                this.comment = comment;
            }

            public WPCException(XElement element)
            {
                this.eLevel = (int)getLevel(element.Attribute("level").Value);
                this.comment = element.Element("Comment").Value;
                this.message = element.Element("Message").Value;
                this.stackTrace = element.Element("StackTrace").Value;
                this.timestamp = DateTime.Parse(element.Attribute("timestamp").Value);
            }

            public String getDebugOutput()
            {
                StringBuilder builder = new StringBuilder();
                builder.Append(timestamp.ToString()); builder.Append(" - ");
                builder.Append(levelLabel[eLevel]); builder.Append(": ");
                builder.Append(this.message);
                if (this.comment != null)
                {
                    builder.Append("\nCOMMENT - "); builder.Append(comment);
                }
                return builder.ToString();
            }

            public String getEmailOutput()
            {
                StringBuilder builder = new StringBuilder();
                builder.Append("\n" + this.timestamp + " - " + levelLabel[this.eLevel]);
                builder.Append("\nMessage: ");
                builder.Append(this.message);
                if (this.comment != null && !this.comment.Equals(""))
                {
                    builder.Append("\nComment: ");
                    builder.Append(this.comment);
                }
                if (this.stackTrace != null && !this.stackTrace.Equals(""))
                {
                    builder.Append("\nStackTrace: \n");
                    builder.Append(this.stackTrace);
                }
                builder.Append("\n--------------------------");
                return builder.ToString();
            }

            public XElement convertToElement()
            {
                XElement Xevent = new XElement("Event");
                XAttribute Xtimestamp = new XAttribute("timestamp", timestamp.ToString());
                XAttribute Xlevel = new XAttribute("level", levelLabel[eLevel]);
                Xevent.Add(Xtimestamp);
                Xevent.Add(Xlevel);
                XElement Xmessage = new XElement("Message");
                Xmessage.Add(this.message);
                XElement Xcomment = new XElement("Comment");
                Xcomment.Add(this.comment);
                XElement Xstacktrace = new XElement("StackTrace");
                Xstacktrace.Add(this.stackTrace);
                Xevent.Add(Xmessage);
                Xevent.Add(Xcomment);
                Xevent.Add(Xstacktrace);
                return Xevent;
            }

            public Boolean isCritical()
            {
                return this.eLevel.Equals((int)LogLevel.critical);
            }
        }
        #endregion
    }
}
