import PostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { withRouter } from 'storybook-addon-react-router-v6';

export default {
  title: 'Pages/PostPage',
  component: PostPage,
  decorators: [withRouter],
} as ComponentMeta<typeof PostPage>;

const Template = (args: any) => <PostPage {...args} />;

export const PostPageTemplate: ComponentStory<typeof PostPage> = Template.bind({});
PostPageTemplate.args = {};
