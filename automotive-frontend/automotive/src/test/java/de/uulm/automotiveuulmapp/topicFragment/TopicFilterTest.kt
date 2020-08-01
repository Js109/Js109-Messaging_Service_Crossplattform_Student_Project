package de.uulm.automotiveuulmapp.topicFragment

import de.uulm.automotiveuulmapp.topic.TopicModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TopicFilterTest {
    @Test
    fun returnsSameOnNoQueryAndNoSubscription() {
        val list = listOf<TopicModel>(
            TopicModel(1, "aligator", "test/aligator", "aligator", arrayOf("alligator"), false),
            TopicModel(1, "backen", "test/backen", "backen", arrayOf("backen"), false),
            TopicModel(1, "computer", "test/computer", "computer", arrayOf("computer"), false)
        )
        val filteredList = TopicFilter.filter(list, "")
        assertThat(filteredList).isEqualTo(list)
    }

    @Test
    fun returnsSubscribedFirstWhenNoFilter() {
        val list = listOf<TopicModel>(
            TopicModel(1, "aligator", "test/aligator", "aligator", arrayOf("alligator"), false),
            TopicModel(2, "backen", "test/backen", "backen", arrayOf("backen"), true),
            TopicModel(3, "computer", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(4, "desktop", "test/desktop", "desktop", arrayOf("desktop"), true)
        )
        val expectedList = listOf<TopicModel>(
            TopicModel(2, "backen", "test/backen", "backen", arrayOf("backen"), true),
            TopicModel(4, "desktop", "test/desktop", "desktop", arrayOf("desktop"), true),
            TopicModel(1, "aligator", "test/aligator", "aligator", arrayOf("alligator"), false),
            TopicModel(3, "computer", "test/computer", "computer", arrayOf("computer"), false)
        )
        assertThat(TopicFilter.filter(list, "")).isNotEqualTo(list)
        assertThat(TopicFilter.filter(list, "")).isEqualTo(expectedList)
    }

    @Test
    fun subscriptionHasNoEffectWhenQuerying() {
        val list = listOf<TopicModel>(
            TopicModel(1, "aac", "test/aac", "aac", arrayOf("aac"), false),
            TopicModel(1, "aad", "test/aad", "aad", arrayOf("aad"), true),
            TopicModel(1, "aaa", "test/aaa", "aaa", arrayOf("aaa"), false),
            TopicModel(1, "aab", "test/aab", "aab", arrayOf("aab"), true)
        )
        val expectedList = listOf<TopicModel>(
            TopicModel(1, "aaa", "test/aaa", "aaa", arrayOf("aaa"), false),
            TopicModel(1, "aab", "test/aab", "aab", arrayOf("aab"), true),
            TopicModel(1, "aac", "test/aac", "aac", arrayOf("aac"), false),
            TopicModel(1, "aad", "test/aad", "aad", arrayOf("aad"), true)
        )
        assertThat(TopicFilter.filter(list, "aa")).isNotEqualTo(list)
        assertThat(TopicFilter.filter(list, "aa")).isEqualTo(expectedList)
    }

    @Test
    fun titleMatchFirstThenDescriptionMatchThenTagMatch() {
        val list = listOf<TopicModel>(
            TopicModel(1, "aligator", "test/aligator", "aligator", arrayOf("computer"), false),
            TopicModel(1, "backen", "test/backen", "computer", arrayOf("backen"), false),
            TopicModel(1, "computer", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(1, "desktop", "test/desktop", "desktop", arrayOf("desktop"), false)
        )
        val expectedList = listOf<TopicModel>(
            TopicModel(1, "computer", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(1, "backen", "test/backen", "computer", arrayOf("backen"), false),
            TopicModel(1, "aligator", "test/aligator", "aligator", arrayOf("computer"), false)
        )
        assertThat(TopicFilter.filter(list, "computer")).isNotEqualTo(list)
        assertThat(TopicFilter.filter(list, "computer")).isEqualTo(expectedList)
    }

    @Test
    fun matchesAreSortedByTitle() {
        val list = listOf<TopicModel>(
            TopicModel(1, "aligatorc", "test/aligator", "aligator", arrayOf("computer"), false),
            TopicModel(1, "aligatora", "test/aligator", "aligator", arrayOf("computer"), false),
            TopicModel(1, "aligatorb", "test/aligator", "aligator", arrayOf("computer"), false),
            TopicModel(1, "backenb", "test/backen", "computer", arrayOf("backen"), false),
            TopicModel(1, "backenc", "test/backen", "computer", arrayOf("backen"), false),
            TopicModel(1, "backena", "test/backen", "computer", arrayOf("backen"), false),
            TopicModel(1, "computera", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(1, "computerb", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(1, "computerc", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(1, "desktop", "test/desktop", "desktop", arrayOf("desktop"), false)
        )
        val expectedList = listOf<TopicModel>(
            TopicModel(1, "computera", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(1, "computerb", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(1, "computerc", "test/computer", "computer", arrayOf("computer"), false),
            TopicModel(1, "backena", "test/backen", "computer", arrayOf("backen"), false),
            TopicModel(1, "backenb", "test/backen", "computer", arrayOf("backen"), false),
            TopicModel(1, "backenc", "test/backen", "computer", arrayOf("backen"), false),
            TopicModel(1, "aligatora", "test/aligator", "aligator", arrayOf("computer"), false),
            TopicModel(1, "aligatorb", "test/aligator", "aligator", arrayOf("computer"), false),
            TopicModel(1, "aligatorc", "test/aligator", "aligator", arrayOf("computer"), false)
        )
        assertThat(TopicFilter.filter(list, "computer")).isNotEqualTo(list)
        assertThat(TopicFilter.filter(list, "computer")).isEqualTo(expectedList)
    }
}