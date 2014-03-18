using System.Threading.Tasks;

namespace MyGuide.Services.Interfaces
{
    public enum DialogType
    {
        YesNo, OkCancel, AllowDenay
    }

    public interface IMessageDialogService
    {
        /// <summary>
        /// Shows dialog
        /// </summary>
        /// <param name="title">
        /// Dialog title.
        /// </param>
        /// <param name="message">
        /// Dialog message.
        /// </param>
        /// <param name="dialogType">
        /// Dialog type.
        /// </param>
        /// <returns>
        /// <b>true</b> - right button was chose
        /// <b>false</b> - left button was chose
        /// </returns>
        Task<bool> ShowDialog(string title, string message, DialogType dialogType);

        /// <summary>
        /// Shows dialog with custom buttons.
        /// </summary>
        /// <param name="title">
        /// Dialog title.
        /// </param>
        /// <param name="message">
        /// Dialog message.
        /// </param>
        /// <param name="leftButton">
        /// Left button text.
        /// </param>
        /// <param name="rightButton">
        /// Right button text.
        /// </param>
        /// <returns>
        /// <b>true</b> - left button was chose
        /// <b>false</b> - right button was chose
        /// </returns>
        Task<bool> ShowDialog(string title, string message, string leftButton, string rightButton);
    }
}