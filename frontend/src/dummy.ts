export const postList: Post[] = [
  {
    id: 1,
    title: '오늘 날씨 맑네요',
    createdAt: '2022-07-19T19:55:31.016376300',
    content: '날씨는 참 좋네요.',
    likeCount: 1200,
    commentCount: 2,
    modified: false,
  },
  {
    id: 2,
    title: '오늘 날씨 좋네요',
    createdAt: '2022-07-18T19:55:31.016376300',
    content: '어항에 있는 것 같아요.',
    likeCount: 4,
    commentCount: 0,
    modified: true,
  },
  {
    id: 3,
    title: '오늘 날씨 싫어요',
    createdAt: '2022-07-11T19:55:31.016376300',
    content: '싫어? 아냐 나 괜찮아',
    likeCount: 3,
    commentCount: 6,
    modified: false,
  },
  {
    id: 4,
    title: '오늘 날씨 별로예요',
    createdAt: '2022-07-10T19:55:31.016376300',
    content: '어항에 있는 것 같아요',
    likeCount: 5,
    commentCount: 0,
    modified: true,
  },
  {
    id: 5,
    title: '조시 : 미안하다 진짜',
    createdAt: '2022-07-09T19:55:31.016376300',
    content: '미안하다 진짜봇',
    likeCount: 3,
    commentCount: 2,
    modified: false,
  },
  {
    id: 6,
    title: '크리스 : 갑자기 이런 생각이 드네?',
    createdAt: '2022-07-08T19:55:31.016376300',
    content: '생각멈춰',
    likeCount: 2,
    commentCount: 2,
    modified: false,
  },
  {
    id: 7,
    title: '이스트 : 아채 싫엉',
    createdAt: '2022-07-07T19:55:31.016376300',
    content: '베이비 이스트',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 8,
    title: '토르 : 사진 찰칵 찰칵',
    createdAt: '2022-07-06T19:55:31.016376300',
    content: '단백질 벌컥 벌컥',
    likeCount: 7,
    commentCount: 7,
    modified: false,
  },
  {
    id: 9,
    title: '헌치 : 악상이 떠오른다.',
    createdAt: '2022-07-05T19:55:31.016376300',
    content: '알고리즘 냠냠',
    likeCount: 10,
    commentCount: 1,
    modified: false,
  },
  {
    id: 10,
    title: '동키콩 : 수학 1등급',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '특 : 페이지 계산 못함',
    likeCount: 1,
    commentCount: 1,
    modified: false,
  },
  {
    id: 11,
    title: '지하철 미션 너무 어렵지 않아요? ㅠㅠ',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '어떻게 해야 할까요?',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 12,
    title: '결국 좋아하는 이성에게 고백했어요..',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '잘한걸까요? ...',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 13,
    title: '오늘 데모데이때 슬리퍼 신어도 될까요?',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '슬리퍼 신고 싶엉',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 14,
    title: '코치님들 진짜...',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '너무 좋다.',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 15,
    title: '속닥속닥팀~~~',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '화이팅!',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 16,
    title: '연재가 뜸했죠? 휴재가 길었습니다.',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '다시 시작합니다.',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 17,
    title: '비 온다며',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '안오네',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 18,
    title: '비 안 온다며',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '오잖아',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 19,
    title: '장염 어떻게 이겨내냐',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '염증은 밥으로 밀어내야한다는 썰이 있던데..',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
  {
    id: 20,
    title: '오늘 머리 감아야되나?',
    createdAt: '2022-07-04T19:55:31.016376300',
    content: '좀 귀찮네',
    likeCount: 3,
    commentCount: 4,
    modified: false,
  },
];

export const memberList: Member[] = [
  {
    username: 'test',
    password: 'test1234',
  },
];

export const validMemberEmail: { email: string; isSignedUp: boolean; code: string; ID?: string; nickname?: string }[] =
  [
    { email: 'test@gmail.com', isSignedUp: true, code: 'test', ID: 'test', nickname: 'test' },
    { email: 'test1@gmail.com', isSignedUp: false, code: 'test' },
  ];

interface CommentListType extends CommentType {
  id: number;
  postId: number;
}

export const commentList: CommentListType[] = [
  {
    id: 1,
    postId: 1,
    content:
      'ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ첫댓글',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 2,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 3,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 4,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 5,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 6,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 7,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 8,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 9,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 10,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 11,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 12,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 13,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 14,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 15,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
  {
    id: 16,
    postId: 1,
    content: '안녕하세요.',
    createdAt: '2022-07-04T19:55:31.016376300',
    nickname: '익명',
  },
];
