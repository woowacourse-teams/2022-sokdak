type Hashtag = {
  id: number;
  name: string;
  count: number;
};

interface Post {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  likeCount: number;
  commentCount: number;
  modified: boolean;
  like: boolean;
  hashtags: Omit<Hashtag, 'count'>[];
  authorized: boolean;
  boardId: number;
  nickname: string;
  blocked: boolean;
  imageName: string;
  viewCount: number;
}

interface Image {
  file?: File;
  src?: string;
  path: string;
}
