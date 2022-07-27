import { act } from 'react-dom/test-utils';
import { QueryClient, QueryClientProvider } from 'react-query';
import { MemoryRouter } from 'react-router-dom';
import * as ReactRouter from 'react-router-dom';

import PostPage from '@/pages/PostPage';

import MockIntersectionObserver from './fixture';
import { postList } from '@/dummy';
import theme from '@/style/theme';
import { ThemeProvider } from '@emotion/react';
import { render, screen, fireEvent, waitFor, prettyDOM } from '@testing-library/react';

window.IntersectionObserver = MockIntersectionObserver;

describe('게시글 상세페이지', () => {
  let id = 1;

  beforeEach(async () => {
    jest.spyOn(ReactRouter, 'useParams').mockReturnValue({ id: String(id) });

    await act(async () => {
      const queryClient = new QueryClient();

      render(
        <QueryClientProvider client={queryClient}>
          <MemoryRouter initialEntries={[`/post/${id}`]}>
            <ThemeProvider theme={theme}>
              <PostPage />
            </ThemeProvider>
          </MemoryRouter>
        </QueryClientProvider>,
      );
    });
  });

  test('좋아요를 누르지 않은 상태에서 좋아요 버튼을 클릭할 경우 좋아요 개수가 증가한다.', async () => {
    const targetPost = postList.find(post => id === post.id)!;
    const { likeCount } = targetPost;
    const likeButton = screen.getByText(String(likeCount));

    await act(async () => {
      fireEvent.click(likeButton);
    });
    expect(likeButton.textContent).toEqual(String(likeCount + 1));
  });

  test('게시글에 댓글을 남길 수 있다.', async () => {
    const input = screen.getByPlaceholderText('댓글을 작성하세요.');

    await act(async () => {
      input.textContent = '새로운 댓글';
      fireEvent.click(screen.getByText('댓글 작성'));
    });

    waitFor(() => {
      expect(input.textContent).toBeEmptyDOMElement();
      expect(screen.getByText('새로운 댓글')).toBeInTheDocument();
    });
  });
});
