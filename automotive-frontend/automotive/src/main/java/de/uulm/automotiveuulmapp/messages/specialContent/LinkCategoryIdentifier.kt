package de.uulm.automotiveuulmapp.messages.specialContent

import java.net.URL

class LinkCategoryIdentifier {
    // Possible types of links
    enum class LinkCategory{
        MAPS,
        YOUTUBE,
        BROWSER
    }

    companion object {
        /**
         * Identifies the category of a URL
         *
         * @param url URL to be identified
         * @return Link type
         */
        fun identify(url: URL) :LinkCategory{
            if(isGoogleMapsUrl(url))
                return LinkCategory.MAPS
            if(isYoutubeUrl(url))
                return LinkCategory.YOUTUBE
            return LinkCategory.BROWSER
        }

        private fun isGoogleMapsUrl(url: URL): Boolean {
            return (url.host.matches(Regex("^(https://)?(www.)?google.[a-z]+"))
                    && url.path.matches(Regex("^/maps/\\S+")))
        }

        private fun isYoutubeUrl(url: URL): Boolean {
            return (url.host.matches(Regex("^((?:https?:)?//)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))"))
                    && url.path.matches(Regex("(/(?:[\\w\\-]+\\?v=|embed/|v/)?)([\\w\\-]+)(\\S+)?")))
        }
    }
}