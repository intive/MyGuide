using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Services.Interfaces
{
    public interface IMessageDialogService
    {
        /// <summary>
        /// Shows dialog with default OK/Cancel buttons.
        /// </summary>
        /// <param name="title">
        /// Dialog title.
        /// </param>
        /// <param name="message">
        /// Dialog message.
        /// </param>
        /// <returns>
        /// <b>true</b> - OK button was chose
        /// <b>false</b> - Cancel button was chose
        /// </returns>
        Task<bool> ShowDialog(string title, string message);

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