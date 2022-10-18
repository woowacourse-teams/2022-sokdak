import { withRouter } from 'storybook-addon-react-router-v6';

import { useContext } from 'react';

import AuthContext from '@/context/Auth';

import Header from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Header',
  component: Header,
  decorators: [withRouter],
} as ComponentMeta<typeof Header>;

const Template: ComponentStory<typeof Header> = () => {
  const { setIsLogin, setUsername } = useContext(AuthContext);
  setIsLogin(true);
  setUsername('hi');
  return <Header />;
};

export const HeaderTemplate = Template.bind({});
