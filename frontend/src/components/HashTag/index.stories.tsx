import { withRouter } from 'storybook-addon-react-router-v6';

import HashTag from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/HashTag',
  component: HashTag,
  decorators: [withRouter],
} as ComponentMeta<typeof HashTag>;

const Template: ComponentStory<typeof HashTag> = args => <HashTag {...args} />;

export const HashTagTemplate = Template.bind({
  name: '생활',
});
