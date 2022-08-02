import { withRouter } from 'storybook-addon-react-router-v6';

import HashTagPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/HashTagPage',
  component: HashTagPage,
  decorators: [withRouter],
} as ComponentMeta<typeof HashTagPage>;

const Template = () => <HashTagPage />;

export const HashTagPageTemplate: ComponentStory<typeof HashTagPage> = Template.bind({});
HashTagPageTemplate.args = {};
