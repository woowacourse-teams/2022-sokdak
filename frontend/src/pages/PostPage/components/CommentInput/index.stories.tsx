import { withRouter } from 'storybook-addon-react-router-v6';

import CommentInput from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/CommentInput',
  component: CommentInput,
  decorators: [withRouter],
} as ComponentMeta<typeof CommentInput>;

const Template: ComponentStory<typeof CommentInput> = args => (
  <div style={{ width: '300px' }}>
    <CommentInput {...args} />
  </div>
);

export const CommentInputTemplate: ComponentStory<typeof CommentInput> = Template.bind({});
CommentInputTemplate.args = {
  amount: 35,
};
