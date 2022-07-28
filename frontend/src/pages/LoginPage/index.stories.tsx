import { withRouter } from 'storybook-addon-react-router-v6';

import LoginPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/LoginPage',
  component: LoginPage,
  decorators: [withRouter],
} as ComponentMeta<typeof LoginPage>;

const Template = () => <LoginPage />;

export const LoginPageTemplate: ComponentStory<typeof LoginPage> = Template.bind({});
LoginPageTemplate.args = {};
