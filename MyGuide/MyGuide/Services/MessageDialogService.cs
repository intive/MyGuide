using Microsoft.Phone.Controls;
using MyGuide.Resources;
using MyGuide.Services.Interfaces;
using System.Threading.Tasks;

namespace MyGuide.Services
{
    public class MessageDialogService : IMessageDialogService
    {
        public Task<bool> ShowDialog(string title, string message, string leftButton, string rightButton)
        {
            var tcs = new TaskCompletionSource<bool>();

            CustomMessageBox messageBox = new CustomMessageBox()
            {
                Caption = title,
                Message = message,
            };
            if (!string.IsNullOrEmpty(rightButton))
                messageBox.RightButtonContent = rightButton;
            messageBox.LeftButtonContent = leftButton;

            messageBox.Dismissed += (s1, e1) =>
            {
                switch (e1.Result)
                {
                    case CustomMessageBoxResult.LeftButton:
                        tcs.TrySetResult(true);
                        break;

                    case CustomMessageBoxResult.RightButton:
                        tcs.TrySetResult(false);
                        break;

                    case CustomMessageBoxResult.None:
                        if (string.IsNullOrEmpty(rightButton))
                        {
                            // Dialog with one button - back key means "ok"
                            tcs.TrySetResult(true);
                        }
                        else
                        {
                            tcs.TrySetResult(false);
                        }
                        break;

                    default:
                        // TODO Log that some thing went wrong
                        tcs.TrySetResult(true);
                        break;
                }
            };

            messageBox.Show();
            return tcs.Task;
        }

        public Task<bool> ShowDialog(string title, string message, DialogType dialogType)
        {
            switch (dialogType)
            {
                case DialogType.YesNo:
                    return ShowDialog(title, message, AppResources.YesButton, AppResources.NoButton);

                case DialogType.OkCancel:
                    return ShowDialog(title, message, AppResources.OkButton, AppResources.CancelButton);

                case DialogType.AllowDenay:
                    return ShowDialog(title, message, AppResources.AllowButton, AppResources.DenayButton);

                default:
                    // TODO Log that some thing went wrong
                    break;
            }
            // TODO Log that some thing went wrong
            return Task.Run<bool>(() => false);
        }
    }
}