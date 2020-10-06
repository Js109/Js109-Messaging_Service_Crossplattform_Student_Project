package de.uulm.automotiveuulmapp.messages

import de.uulm.automotiveuulmapp.messages.specialContent.LinkCategoryIdentifier
import org.assertj.core.api.Assertions
import org.junit.Test
import java.net.URL

class MessageActivityTest {
    @Test
    fun isYoutubeLink() {
        val list = listOf("https://www.youtube.com/attribution_link?a=tolCzpA7CrY&u=%2Fwatch%3Fv%3DMoBL33GT9S8%26feature%3Dshare",
        "https://www.youtube.com/watch?v=MoBL33GT9S8&feature=share",
        "http://www.youtube.com/watch?v=iwGFalTRHDA",
        "https://www.youtube.com/watch?v=iwGFalTRHDA",
        "http://www.youtube.com/watch?v=iwGFalTRHDA&feature=related",
        "http://youtu.be/iwGFalTRHDA",
        "http://www.youtube.com/embed/watch?feature=player_embedded&v=iwGFalTRHDA",
        "http://www.youtube.com/embed/watch?v=iwGFalTRHDA",
        "http://www.youtube.com/embed/v=iwGFalTRHDA",
        "http://www.youtube.com/watch?feature=player_embedded&v=iwGFalTRHDA",
        "http://www.youtube.com/watch?v=iwGFalTRHDA",
        "https://www.youtube.com/watch?v=iwGFalTRHDA",
        "https://www.youtu.be/iwGFalTRHDA",
        "https://youtu.be/iwGFalTRHDA",
        "https://youtube.com/watch?v=iwGFalTRHDA",
        "http://www.youtube.com/watch/iwGFalTRHDA",
        "http://www.youtube.com/v/iwGFalTRHDA",
        "http://www.youtube.com/v/i_GFalTRHDA",
        "http://www.youtube.com/watch?v=i-GFalTRHDA&feature=related",
        "http://www.youtube.com/attribution_link?u=/watch?v=aGmiw_rrNxk&feature=share&a=9QlmP1yvjcllp0h3l0NwuA",
        "http://www.youtube.com/attribution_link?a=fF1CWYwxCQ4&u=/watch?v=qYr8opTPSaQ&feature=em-uploademail",
        "http://www.youtube.com/attribution_link?a=fF1CWYwxCQ4&feature=em-uploademail&u=/watch?v=qYr8opTPSaQ"
        )
        list.forEach{ link ->
            Assertions.assertThat(LinkCategoryIdentifier.identify(URL(link)) == LinkCategoryIdentifier.LinkCategory.YOUTUBE)
        }
    }

    @Test
    fun isMapsLink(){
        val list = listOf(
            "https://www.google.com/maps",
            "https://www.google.fr/maps",
            "https://google.fr/maps",
            "http://google.fr/maps",
            "https://www.google.de/maps",
            "https://www.google.com/maps?ll=37.0625,-95.677068&spn=45.197878,93.076172&t=h&z=4",
            "https://www.google.de/maps?ll=37.0625,-95.677068&spn=45.197878,93.076172&t=h&z=4",
            "https://www.google.com/maps?ll=37.0625,-95.677068&spn=45.197878,93.076172&t=h&z=4&layer=t&lci=com.panoramio.all,com.google.webcams,weather",
            "https://www.google.com/maps?ll=37.370157,0.615234&spn=45.047033,93.076172&t=m&z=4&layer=t",

            "https://www.google.com/maps?ll=37.0625,-95.677068&spn=45.197878,93.076172&t=h&z=4",
            "https://www.google.de/maps?ll=37.0625,-95.677068&spn=45.197878,93.076172&t=h&z=4",
            "https://www.google.com/maps?ll=37.0625,-95.677068&spn=45.197878,93.076172&t=h&z=4&layer=t&lci=com.panoramio.all,com.google.webcams,weather",
            "https://www.google.com/maps?ll=37.370157,0.615234&spn=45.047033,93.076172&t=m&z=4&layer=t",

            "https://www.google.de/maps/@48.3998313,9.9816747,15z"
        )
        list.forEach{ link ->
            Assertions.assertThat(LinkCategoryIdentifier.identify(URL(link)) == LinkCategoryIdentifier.LinkCategory.MAPS)
        }
    }
}