import MainPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { withRouter } from 'storybook-addon-react-router-v6';

export default {
  title: 'Pages/MainPage',
  component: MainPage,
  decorators: [withRouter],
} as ComponentMeta<typeof MainPage>;

const Template = () => <MainPage />;

export const MainPageTemplate: ComponentStory<typeof MainPage> = Template.bind({});
MainPageTemplate.args = {};
