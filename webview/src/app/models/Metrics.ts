export interface Metrics {
  sentMessagesTotalAllTime: number;
  sentMessagesTotalAtBegin: number;
  sentMessagesTotalGain: number;
  scheduledMessagesTotalAllTime: number;
  scheduledMessagesTimeSpan: number;
  subscriberTotalAllTime: number;
  subscriberTotalAtBegin: number;
  subscriberTotalGainOverTimespan: number;
  averageMessageLengthAllTime: number;
  averageMessageLengthTimeSpan: number;
  mostActiveSenderAllTime: string;
  mostActiveSenderTimeSpan: string;
  mostActiveWeekdayAllTime: string;
  mostActiveWeekdayTimeSpan: string;
  sentMessagesByTimeOfDayAllTime: Map<number, number>;
  sentMessagesByTimeOfDayTimeSpan: Map<number, number>;
  sentMessagesByDateTimeSpan: Map<string, number>;
  scheduledMessagesByDateTimeSpan: Map<string, number>;
  subscriberGainByDateTimeSpan: Map<string, number>;
}
