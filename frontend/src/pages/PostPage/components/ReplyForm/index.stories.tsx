import { withRouter } from 'storybook-addon-react-router-v6';

import ReplyForm from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/ReplyInput',
  component: ReplyForm,
  decorators: [withRouter],
} as ComponentMeta<typeof ReplyForm>;

const Template: ComponentStory<typeof ReplyForm> = args => (
  <div style={{ width: '314px' }}>
    <ReplyForm {...args} />
  </div>
);

export const ReplyInputTemplate: ComponentStory<typeof ReplyForm> = Template.bind({});
ReplyInputTemplate.args = {
  commentId: 1,
};
