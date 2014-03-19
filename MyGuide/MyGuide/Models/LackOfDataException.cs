using System;

namespace MyGuide.Models
{
    public class LackOfDataException : Exception
    {
        public LackOfDataException()
            : base()
        {
        }

        public LackOfDataException(string message)
            : base(message)
        {
        }

        public LackOfDataException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}