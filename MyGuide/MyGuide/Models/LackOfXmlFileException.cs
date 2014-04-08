using System;

namespace MyGuide.Models
{
    public class LackOfXmlFileException : Exception
    {
        public LackOfXmlFileException()
            : base()
        {
        }

        public LackOfXmlFileException(string message)
            : base(message)
        {
        }

        public LackOfXmlFileException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}