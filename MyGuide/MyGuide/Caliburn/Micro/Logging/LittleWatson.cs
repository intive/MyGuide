using Microsoft.Phone.Info;
using Microsoft.Phone.Net.NetworkInformation;
using MyGuide;
using MyGuide.Resources;
using MyGuide.Services.Interfaces;
using NLog.Config;
using NLog.Targets;
using SendGridSDK;
using System;
using System.IO;
using System.IO.IsolatedStorage;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using Windows.Networking.Connectivity;

namespace Caliburn.Micro.Logging
{
    public class LittleWatson
    {
        private readonly string PASSWORD = "AxYGMXtd3ykWi5u";
        private readonly string SPACE = "----------------------------------------------------------------------";
        private readonly string USER = "azure_84dfa928e4534d960e91f357a25119c1@azure.com";
        private ILog _log;
        private IMessageDialogService _messageDialogService;

        public LittleWatson(IMessageDialogService messageDialogService)
        {
            _messageDialogService = messageDialogService;
            _log = LogManager.GetLog(typeof(LittleWatson));
        }

        public async Task SendReport(Exception e)
        {
            _log.Error(e);
            bool send = await _messageDialogService.ShowDialog(AppResources.RaportTitle,
                AppResources.RaportMessage, DialogType.YesNo);
            if (send)
            {
                _log.Info("LittleWatson try to send raport");
                if (await IsNetworkAvaliable())
                {
                    _messageDialogService.ShowDialog(AppResources.SendingRaportTitle,
                        AppResources.SendingRaportMessage, null, null);

                    string raport = GetRaport().Replace(System.Environment.NewLine, "<br>");
                    var sendGridMail = new Mail(USER, PASSWORD);
                    sendGridMail.addTo("bukalski.piotr@gmail.com")
                        //.addTo("pat2014-wro-wp-list@blstream.com")
                                .setFrom("UserReport@myguide.com")
                                .setSubject("MyGuide crash report")
                                .setHtml(raport);
                    await sendGridMail.send();
                }
                else
                {
                    _log.Warn("Network not available sending report canceled");
                    await _messageDialogService.ShowDialog(AppResources.NetworkNotAvailableTitle,
                        AppResources.NetworkNotAvailableMessage, "ok", null);
                }
            }
            else
            {
                _log.Info("LittleWatson denied sending raport");
            }
            App.Current.Terminate();
        }

        private string GetRaport()
        {
            StringBuilder raport = new StringBuilder();
            raport.AppendLine("<h2>MyGuide Crash Report</h2>");

            raport.AppendLine("<b>Device details</b>");
            PropertyInfo[] properties = typeof(DeviceStatus).GetProperties();
            properties.Apply(p =>
            {
                raport.AppendFormat("{0}: {1}{2}", p.Name, p.GetValue(null), System.Environment.NewLine);
            });

            raport.AppendLine("<b>User details</b>");
            try
            {
                var userID = UserExtendedProperties.GetValue("ANID2") as string;
                if (userID != null)
                {
                    raport.AppendLine(userID);
                }
                else
                {
                    _log.Warn("Cannot get UserExtendedProperties ANID2 is null");
                    raport.AppendLine("none");
                }
            }
            catch (Exception e)
            {
                _log.Warn("Cannot get UserExtendedProperties ANID2: ", e);
                raport.AppendLine("none");
            }

            raport.AppendLine(ReadLogs());
            return raport.ToString();
        }

        private async Task<bool> IsNetworkAvaliable()
        {
            _log.Info("Network: {0} {1}", NetworkInterface.NetworkInterfaceType,
                NetworkInterface.GetIsNetworkAvailable());
            if (NetworkInterface.NetworkInterfaceType == NetworkInterfaceType.MobileBroadbandGsm
                || NetworkInterface.NetworkInterfaceType == NetworkInterfaceType.MobileBroadbandCdma)
            {
                return await _messageDialogService.ShowDialog(AppResources.OnlyGsmTitle,
                        AppResources.OnlyGsmMessage, DialogType.YesNo);
            }
            return NetworkInterface.GetIsNetworkAvailable();
        }

        private string ReadLogs()
        {
            StringBuilder result = new StringBuilder();
            result.AppendLine(SPACE);
            result.AppendLine("<b>LOGS</b>");
            result.AppendLine(SPACE);

            LoggingConfiguration nLogConfig = global::NLog.LogManager.Configuration;
            foreach (Target target in nLogConfig.AllTargets)
            {
                IsolatedStorageTarget ISFileTarget = target as IsolatedStorageTarget;
                if (ISFileTarget != null)
                {
                    result.AppendLine("<b>" + ISFileTarget.FileName + "</b>");
                    ISFileTarget.Flush(e =>
                    {
                        if (e != null)
                        {
                            _log.Error(e);
                        }
                    });
                    using (var isolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
                    {
                        using (var fileStream = new IsolatedStorageFileStream(ISFileTarget.FileName, FileMode.Open, FileAccess.Read, FileShare.ReadWrite, isolatedStorage))
                        {
                            using (StreamReader reader = new StreamReader(fileStream))
                            {
                                result.AppendLine(reader.ReadToEnd());
                                result.AppendLine(SPACE);
                            }
                        }
                    }
                }
            }
            return result.ToString();
        }
    }
}