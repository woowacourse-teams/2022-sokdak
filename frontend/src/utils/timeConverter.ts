const options = {
  year: 'numeric',
  month: 'long',
  day: 'numeric',
} as const;

const timeConverter = (localDate: { date: string; time: string }): string => {
  const postDate = new Date(localDate.date + 'T' + localDate.time);
  const sec = (Date.now() - +postDate) / 1000;
  const date = Math.floor(sec / 60 / 60 / 24);
  const hour = Math.floor(sec / 60 / 60);
  const min = Math.floor(sec / 60);

  if (date > 5) return new Intl.DateTimeFormat('ko-KR', options).format(postDate);
  if (hour >= 24) return date + '일 전';
  if (min >= 60) return hour + '시간 전';
  if (sec >= 60) return min + '분 전';
  return '방금 전';
};

export default timeConverter;
