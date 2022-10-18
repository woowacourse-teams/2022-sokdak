import { withRouter } from 'storybook-addon-react-router-v6';

import Sidebar from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/PostPage/Sidebar',
  component: Sidebar,
  decorators: [withRouter],
} as ComponentMeta<typeof Sidebar>;

const Template: ComponentStory<typeof Sidebar> = args => <Sidebar {...args} />;

export const SidebarTemplate: ComponentStory<typeof Sidebar> = Template.bind({});
SidebarTemplate.args = {
  title: '실시간 인기글',
  items: [
    {
      name: '위니, 앨버랑 노는중 ~',
      url: '',
    },
    {
      name: '너무 재밌당 ~~',
      url: '',
    },
  ],
};
