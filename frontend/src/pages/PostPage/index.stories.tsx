import PostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/PostPage',
  component: PostPage,
} as ComponentMeta<typeof PostPage>;

const Template = (args: any) => <PostPage {...args} />;

export const PostPageTemplate: ComponentStory<typeof PostPage> = Template.bind({});
PostPageTemplate.args = {};
