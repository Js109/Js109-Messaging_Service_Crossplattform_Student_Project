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
  sentMessagesByTimeOfDayAllTime: any;
  sentMessagesByTimeOfDayTimeSpan: any;
  sentMessagesByDateTimeSpan: any;
  scheduledMessagesByDateTimeSpan: any;
  subscriberGainByDateTimeSpan: any;
}
