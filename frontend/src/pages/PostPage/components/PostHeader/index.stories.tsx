import { withRouter } from 'storybook-addon-react-router-v6';

import Layout from '@/components/@styled/Layout';

import PostHeader from '.';
import { postList } from '@/dummy';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/PostHeader',
  component: PostHeader,
  decorators: [withRouter],
} as ComponentMeta<typeof PostHeader>;

const Template: ComponentStory<typeof PostHeader> = () => {
  return (
    <Layout>
      <PostHeader post={postList[1]} onClickDeleteButton={() => {}} onClickLikeButton={() => {}} />
    </Layout>
  );
};

export const PostHeaderTemplate: ComponentStory<typeof PostHeader> = Template.bind({});
PostHeaderTemplate.bind({});
