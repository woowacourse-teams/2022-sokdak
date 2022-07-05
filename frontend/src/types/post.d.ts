export {};

declare global {
  interface Post {
    id: number;
    title: string;
    localDate: {
      date: string;
      time: string;
    };
    content: string;
  }
}
