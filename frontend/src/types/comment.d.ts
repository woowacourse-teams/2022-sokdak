interface CommentType {
  id: number;
  nickname: string | null;
  content: string | null;
  createdAt: string | null;
  authorized: boolean | null;
  likeCount: number;
}
