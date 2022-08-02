import { withRouter } from 'storybook-addon-react-router-v6';

import BoardPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/BoardPage',
  component: BoardPage,
  decorators: [withRouter],
} as ComponentMeta<typeof BoardPage>;

const Template = () => <BoardPage />;

export const BoardPageTemplate: ComponentStory<typeof BoardPage> = Template.bind({});
BoardPageTemplate.args = {};
