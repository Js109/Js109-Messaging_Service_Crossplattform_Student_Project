package de.uulm.automotiveuulmapp.messages

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MessageContentPruneKtTest {

    private fun pruneAndAssertNoChange(testString: String) {
        assertThat(testString.pruneMessageTags()).isEqualTo(testString)
    }

    @Test
    fun `no tag creates no change`() {
        pruneAndAssertNoChange("test")
        pruneAndAssertNoChange("Text with spaces.\nAnd also new space and other Symbols:{not a tag}[also not a tag](not a tag either)")
        pruneAndAssertNoChange("[image][im][ig][gm][mig]")
        pruneAndAssertNoChange("[text](lnk1)[text](lin2)[text](ink1)[text](lik1)")
    }

    @Test
    fun `case has no effect`() {
        assertThat("[img]".pruneMessageTags()).isEqualTo("")
        assertThat("[Img]".pruneMessageTags()).isEqualTo("")
        assertThat("[IMG]".pruneMessageTags()).isEqualTo("")

        assertThat("[linktext](link1)".pruneMessageTags()).isEqualTo("linktext")
        assertThat("[linktext](Link1)".pruneMessageTags()).isEqualTo("linktext")
        assertThat("[linktext](LINK1)".pruneMessageTags()).isEqualTo("linktext")
    }

    @Test
    fun `words outside of tags stay unchanged`() {
        assertThat("Text before a link [linktext](link1)".pruneMessageTags()).isEqualTo("Text before a link linktext")
        assertThat("Text around [linktext](link1) of a link".pruneMessageTags()).isEqualTo("Text around linktext of a link")
        assertThat("[linktext](link1) text after a link".pruneMessageTags()).isEqualTo("linktext text after a link")

        assertThat("[img] text after an image".pruneMessageTags()).isEqualTo(" text after an image")
        assertThat("text around [img] an image".pruneMessageTags()).isEqualTo("text around  an image")
        assertThat("[img] text after an image".pruneMessageTags()).isEqualTo(" text after an image")
    }
}