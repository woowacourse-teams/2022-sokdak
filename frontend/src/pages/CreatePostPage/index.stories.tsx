import CreatePostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { withRouter } from 'storybook-addon-react-router-v6';

export default {
  title: 'Pages/CreatePostPage',
  component: CreatePostPage,
  decorators: [withRouter],
} as ComponentMeta<typeof CreatePostPage>;

const Template = (args: any) => <CreatePostPage {...args} />;

export const CreatePostPageTemplate: ComponentStory<typeof CreatePostPage> = Template.bind({});
CreatePostPageTemplate.args = {};
