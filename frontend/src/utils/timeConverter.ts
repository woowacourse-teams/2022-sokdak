const options = {
  year: 'numeric',
  month: 'long',
  day: 'numeric',
} as const;

const timeConverter = (createdAt: string): string => {
  const postDate = new Date(createdAt);
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

const isToday = (dateParams: Date) => {
  const day = new Date();
  return (
    dateParams.getFullYear() === day.getFullYear() &&
    dateParams.getMonth() === day.getMonth() &&
    dateParams.getDate() === day.getDate()
  );
};

const isYesterday = (dateParams: Date) => {
  const day = new Date();
  return (
    dateParams.getFullYear() === day.getFullYear() &&
    dateParams.getMonth() === day.getMonth() &&
    dateParams.getDate() === day.getDate() - 1
  );
};

const isInWeek = (dateParams: Date) => {
  const diffDate =
    Math.floor(Number(new Date()) / 1000 / 60 / 60 / 24) - Math.floor(Number(dateParams) / 1000 / 60 / 60 / 24);
  if (diffDate >= 7 || diffDate < 0) return false;
  return new Date().getDay() >= dateParams.getDay();
};

const isInMonth = (dateParams: Date) => {
  const day = new Date();
  return dateParams.getFullYear() === day.getFullYear() && dateParams.getMonth() === day.getMonth();
};

export const convertToTimeType = (createdAt: Date): TimeType => {
  if (isToday(createdAt)) {
    return '오늘';
  }
  if (isYesterday(createdAt)) {
    return '어제';
  }
  if (isInWeek(createdAt)) {
    return '이번주';
  }
  if (isInMonth(createdAt)) {
    return '이번달';
  }
  return '오래전';
};

export default timeConverter;
