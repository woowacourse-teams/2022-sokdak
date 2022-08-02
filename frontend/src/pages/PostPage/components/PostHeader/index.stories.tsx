import { withRouter } from 'storybook-addon-react-router-v6';

import Layout from '@/components/@styled/Layout';

import PostHeader from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/PostHeader',
  component: PostHeader,
  decorators: [withRouter],
} as ComponentMeta<typeof PostHeader>;

const Template: ComponentStory<typeof PostHeader> = () => {
  return (
    <Layout>
      <PostHeader
        post={{
          id: 1,
          title: '오늘 날씨 맑네요',
          createdAt: '2022-07-19T19:55:31.016376300',
          content: '날씨는 참 좋네요.',
          likeCount: 19,
          commentCount: 16,
          like: false,
          modified: false,
          hashtags: [
            {
              id: 1,
              name: '생활',
            },
            {
              id: 2,
              name: '조현근',
            },
          ],
          authorized: false,
          boardId: 1,
        }}
        like={{
          isLiked: false,
          likeCount: 2,
        }}
        onClickDeleteButton={() => {}}
        onClickLikeButton={() => {}}
      />
    </Layout>
  );
};

export const PostHeaderTemplate: ComponentStory<typeof PostHeader> = Template.bind({});
PostHeaderTemplate.bind({
  post: {
    id: 1,
    title: '오늘 날씨 맑네요',
    createdAt: '2022-07-19T19:55:31.016376300',
    content: '날씨는 참 좋네요.',
    likeCount: 19,
    commentCount: 16,
    like: false,
    modified: false,
    hashtags: [
      {
        id: 1,
        name: '생활',
      },
      {
        id: 2,
        name: '조현근',
      },
    ],
    authorized: false,
    boardId: 1,
  },
});
