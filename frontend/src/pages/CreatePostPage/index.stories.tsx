import { withRouter } from 'storybook-addon-react-router-v6';

import CreatePostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/CreatePostPage',
  component: CreatePostPage,
  decorators: [withRouter],
} as ComponentMeta<typeof CreatePostPage>;

const Template = () => <CreatePostPage />;

export const CreatePostPageTemplate: ComponentStory<typeof CreatePostPage> = Template.bind({});
CreatePostPageTemplate.args = {};
