import { withRouter } from 'storybook-addon-react-router-v6';

import SignUpPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/SignUpPage',
  component: SignUpPage,
  decorators: [withRouter],
} as ComponentMeta<typeof SignUpPage>;

const Template = () => <SignUpPage />;

export const SignUpPageTemplate: ComponentStory<typeof SignUpPage> = Template.bind({});
SignUpPageTemplate.args = {};
