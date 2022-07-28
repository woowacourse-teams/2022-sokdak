import { act } from 'react-dom/test-utils';
import { QueryClient, QueryClientProvider } from 'react-query';
import { MemoryRouter } from 'react-router-dom';
import * as ReactRouter from 'react-router-dom';

import PostPage from '@/pages/PostPage';

import MockIntersectionObserver from './fixture';
import App from '@/App';
import { postList } from '@/dummy';
import theme from '@/style/theme';
import { ThemeProvider } from '@emotion/react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';

window.IntersectionObserver = MockIntersectionObserver;

describe('게시글 상세페이지', () => {
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
    const input = await screen.findByPlaceholderText('댓글을 작성하세요.');

    input.textContent = '새로운 댓글';
    fireEvent.click(screen.getByText('댓글 작성'));

    waitFor(() => {
      expect(input.textContent).toBeEmptyDOMElement();
      expect(screen.getByText('새로운 댓글')).toBeInTheDocument();
    });
  });
});

describe('글 보기 테스트', () => {
  const postId = 2;

  beforeAll(() => {
    jest.spyOn(ReactRouter, 'useParams').mockReturnValue({ id: String(postId) });
  });

  beforeEach(async () => {
    const queryClient = new QueryClient();
    const promise = Promise.resolve();

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={[`/`]}>
          <ThemeProvider theme={theme}>
            <App />
          </ThemeProvider>
        </MemoryRouter>
      </QueryClientProvider>,
    );

    await act(async () => {
      return promise;
    });
  });

  test('글 목록에서 특정 글을 클릭하면 해당 글 정보를 볼 수 있다.', async () => {
    fireEvent.click(await screen.findByTestId(postId));

    const targetPost = postList.find(post => postId === post.id)!;

    expect(await screen.findByText(targetPost.title)).toBeInTheDocument();
    expect(await screen.findByText(targetPost.content)).toBeInTheDocument();
  });
});
