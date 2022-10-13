import { withRouter } from 'storybook-addon-react-router-v6';

import Carousel from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/MainPage/Carousel',
  component: Carousel,
  decorators: [withRouter],
} as ComponentMeta<typeof Carousel>;

const Template = () => (
  <div style={{ width: '100%' }}>
    <Carousel />
  </div>
);

export const CarouselTemplate: ComponentStory<typeof Carousel> = Template.bind({});
CarouselTemplate.args = {};
