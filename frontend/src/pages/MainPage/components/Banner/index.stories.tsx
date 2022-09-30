import { withRouter } from 'storybook-addon-react-router-v6';

import Banner from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/MainPage/Banner',
  component: Banner,
  decorators: [withRouter],
} as ComponentMeta<typeof Banner>;

const Template = () => (
  <div style={{ width: '100%' }}>
    <Banner />
  </div>
);

export const BannerTemplate: ComponentStory<typeof Banner> = Template.bind({});
BannerTemplate.args = {};
