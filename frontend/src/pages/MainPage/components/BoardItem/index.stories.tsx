import { withRouter } from 'storybook-addon-react-router-v6';

import BoardItem from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/BoardItem',
  component: BoardItem,
  decorators: [withRouter],
} as ComponentMeta<typeof BoardItem>;

const Template: ComponentStory<typeof BoardItem> = args => (
  <div style={{ maxWidth: '400px' }}>
    <BoardItem {...args} />
  </div>
);

export const BoardItemTemplate: ComponentStory<typeof BoardItem> = Template.bind({});
BoardItemTemplate.args = {
  id: 1,
  posts: [
    { commentCount: 3, id: 1, likeCount: 4, title: '나는야 조현근' },
    { commentCount: 3, id: 2, likeCount: 4, title: '나는야 조현근' },
    { commentCount: 3, id: 3, likeCount: 4, title: '나는야 조현근' },
  ],
  title: '🔥핫게시판🔥',
};
