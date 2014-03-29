using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Services.Maps.TileSources
{
    public class OSMRenderTileSource : Microsoft.Phone.Controls.Maps.TileSource
    {
        private readonly static string[] TilePathPrefixes =
            new[] { "a", "b" };

        private readonly Random _rand;

        public OSMRenderTileSource()
        {
            UriFormat = "http://{0}.tile.openstreetmap.org/{1}/{2}/{3}.png";
            _rand = new Random();
        }

        private string Server
        {
            get
            {
                return TilePathPrefixes[_rand.Next(2)];
            }
        }

        public override Uri GetUri(int x, int y, int zoomLevel)
        {
            if (zoomLevel > 0)
            {
                var url = string.Format(UriFormat, Server, zoomLevel, x, y);
                return new Uri(url);
            }
            return null;
        }
    }
}