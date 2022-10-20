import { withRouter } from 'storybook-addon-react-router-v6';

import Post from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/SearchModal/SearchedPost/Post',
  component: Post,
  decorators: [withRouter],
} as ComponentMeta<typeof Post>;

const Template: ComponentStory<typeof Post> = args => <Post {...args} />;

export const SearchModalTemplate = Template.bind({});
SearchModalTemplate.args = {
  id: 1,
  title: '조시 논란',
  content: '그 있잖아 조시 말이야 집가서 밥 더 먹는대..!',
  keyword: '조시',
};
