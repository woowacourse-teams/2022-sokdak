import { withRouter } from 'storybook-addon-react-router-v6';

import MainPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/MainPage',
  component: MainPage,
  decorators: [withRouter],
} as ComponentMeta<typeof MainPage>;

const Template = () => (
  <div style={{ width: '400px' }}>
    <MainPage />
  </div>
);

export const MainPageTemplate: ComponentStory<typeof MainPage> = Template.bind({});
MainPageTemplate.args = {};
