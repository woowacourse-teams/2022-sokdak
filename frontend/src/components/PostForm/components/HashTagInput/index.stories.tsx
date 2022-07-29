import { useState } from 'react';

import HashTagInput from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/HashTagInput',
  component: HashTagInput,
  parameters: {
    layout: 'centered',
  },
} as ComponentMeta<typeof HashTagInput>;

const Template: ComponentStory<typeof HashTagInput> = () => {
  const [hashtags, setHashtags] = useState<string[]>([]);

  return (
    <div style={{ width: '300px' }}>
      <HashTagInput hashtags={hashtags} setHashtags={setHashtags} />
    </div>
  );
};

export const HashTagInputTemplate = Template.bind({});
HashTagInputTemplate.args = {};
