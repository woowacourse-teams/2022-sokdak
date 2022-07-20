type Hashtag = {
  id: number;
  name: string;
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
  hashtags: Hashtag[];
}
