using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Services
{
    public class MessageDialogService : IMessageDialogService
    {
        public Task<bool> ShowDialog(string title, string message)
        {
            throw new NotImplementedException();
        }

        public Task<bool> ShowDialog(string title, string message, string leftButton, string rightButton)
        {
            throw new NotImplementedException();
        }
    }
}