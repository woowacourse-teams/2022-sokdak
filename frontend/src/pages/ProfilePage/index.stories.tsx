import { withRouter } from 'storybook-addon-react-router-v6';

import ProfilePage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/ProfilePage/Page',
  component: ProfilePage,
  decorators: [withRouter],
} as ComponentMeta<typeof ProfilePage>;

const Template = () => <ProfilePage />;

export const ProfilePageTemplate: ComponentStory<typeof ProfilePage> = Template.bind({});
ProfilePageTemplate.args = {};
