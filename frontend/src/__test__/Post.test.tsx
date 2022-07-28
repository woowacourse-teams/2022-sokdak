import { QueryClient, QueryClientProvider } from 'react-query';
import { MemoryRouter } from 'react-router-dom';
import * as ReactRouter from 'react-router-dom';

import PostPage from '@/pages/PostPage';

import { AuthContextProvider } from '@/context/Auth';
import { SnackBarContextProvider } from '@/context/Snackbar';

import App from '@/App';
import { memberList, postList } from '@/dummy';
import theme from '@/style/theme';
import { ThemeProvider } from '@emotion/react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';

describe('게시글 상세 페이지 테스트', () => {
  const postId = 1;

  beforeAll(() => {
    jest.spyOn(ReactRouter, 'useParams').mockReturnValue({ id: String(postId) });
  });

  beforeEach(async () => {
    const queryClient = new QueryClient();

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={[`/post/${postId}`]}>
          <ThemeProvider theme={theme}>
            <PostPage />
          </ThemeProvider>
        </MemoryRouter>
      </QueryClientProvider>,
    );
  });

  test('좋아요를 누르지 않은 상태에서 좋아요 버튼을 클릭할 경우 좋아요 개수가 증가한다.', async () => {
    const targetPost = postList.find(post => postId === post.id)!;
    const { likeCount } = targetPost;
    const likeButton = await screen.findByText(String(likeCount));

    fireEvent.click(likeButton);

    waitFor(() => {
      expect(likeButton.textContent).toEqual(String(likeCount + 1));
    });
  });

  test('게시글에 댓글을 남길 수 있다.', async () => {
    const commentInput = await screen.findByPlaceholderText('댓글을 작성하세요.');

    commentInput.textContent = '새로운 댓글';
    fireEvent.click(screen.getByText('댓글 작성'));

    waitFor(() => {
      expect(commentInput.textContent).toBeEmptyDOMElement();
      expect(screen.getByText('새로운 댓글')).toBeInTheDocument();
    });
  });
});

describe('게시글 확인 테스트', () => {
  const postId = 2;

  beforeAll(() => {
    jest.spyOn(ReactRouter, 'useParams').mockReturnValue({ id: String(postId) });
  });

  beforeEach(async () => {
    const queryClient = new QueryClient();

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={[`/`]}>
          <ThemeProvider theme={theme}>
            <App />
          </ThemeProvider>
        </MemoryRouter>
      </QueryClientProvider>,
    );
  });

  test('글 목록에서 특정 글을 클릭하면 해당 글 정보를 볼 수 있다.', async () => {
    fireEvent.click(await screen.findByTestId(postId));

    const targetPost = postList.find(post => postId === post.id)!;

    expect(await screen.findByText(targetPost.title)).toBeInTheDocument();
    expect(await screen.findByText(targetPost.content)).toBeInTheDocument();
  });
});

describe('게시글 작성 테스트', () => {
  beforeEach(async () => {
    const queryClient = new QueryClient();
    const snackBarElement = document.createElement('div');
    snackBarElement.id = 'snackbar';

    render(
      <SnackBarContextProvider>
        <AuthContextProvider>
          <QueryClientProvider client={queryClient}>
            <MemoryRouter initialEntries={[`/`]}>
              <ThemeProvider theme={theme}>
                <App />
              </ThemeProvider>
            </MemoryRouter>
          </QueryClientProvider>
        </AuthContextProvider>
      </SnackBarContextProvider>,
    );

    if (!document.getElementById('snackbar')) document.body.appendChild(snackBarElement);

    async function login() {
      fireEvent.click(await screen.findByText('로그인'));

      const IDInput = await screen.findByPlaceholderText('아이디');
      const passwordInput = await screen.findByPlaceholderText('비밀번호');
      const submitButton = (await screen.findAllByText('로그인')).find(button => button.tagName === 'BUTTON');
      const [targetMember] = memberList;

      fireEvent.change(IDInput, { target: { value: targetMember.username } });
      fireEvent.change(passwordInput, { target: { value: targetMember.password } });
      fireEvent.click(submitButton!);

      expect(await screen.findByText(targetMember.username[0])).toBeInTheDocument();
      expect(screen.queryByText('로그인')).toBe(null);
    }

    await login();
  });

  test('로그인한 사용자는 글을 작성할 수 있다.', async () => {
    fireEvent.click(await screen.findByText('+'));

    const titleInput = await screen.findByPlaceholderText('제목을 입력해주세요.');
    const contentInput = await screen.findByPlaceholderText('내용을 작성해주세요.');
    const tagInput = await screen.findByPlaceholderText('태그를 입력해주세요.');

    fireEvent.change(titleInput, { target: { value: '입력된 제목' } });
    fireEvent.change(contentInput, { target: { value: '입력된 내용' } });
    fireEvent.change(tagInput, { target: { value: '새로운 태그,' } });
    fireEvent.click(await screen.findByText('글 작성하기'));

    expect(await screen.findByTestId(postList.length + 1)).toBeInTheDocument();
    expect(await screen.findByText('입력된 제목')).toBeInTheDocument();
    expect(await screen.findByText('입력된 내용')).toBeInTheDocument();
  });
});
