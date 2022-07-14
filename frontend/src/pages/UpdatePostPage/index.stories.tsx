import { withRouter } from 'storybook-addon-react-router-v6';

import UpdatePostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/UpdatePostPage',
  component: UpdatePostPage,
  decorators: [withRouter],
} as ComponentMeta<typeof UpdatePostPage>;

const Template = () => <UpdatePostPage />;

export const UpdatePostPageTemplate: ComponentStory<typeof UpdatePostPage> = Template.bind({});
UpdatePostPageTemplate.args = {};
