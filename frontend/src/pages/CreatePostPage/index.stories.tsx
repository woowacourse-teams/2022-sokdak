import CreatePostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/CreatePostPage',
  component: CreatePostPage,
} as ComponentMeta<typeof CreatePostPage>;

const Template = (args: any) => <CreatePostPage {...args} />;

export const CreatePostPageTemplate: ComponentStory<typeof CreatePostPage> = Template.bind({});
CreatePostPageTemplate.args = {};
