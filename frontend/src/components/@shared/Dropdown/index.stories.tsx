import Layout from '@/components/@styled/Layout';

import Dropdown from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Dropdown',
  component: Dropdown,
} as ComponentMeta<typeof Dropdown>;

const Template: ComponentStory<typeof Dropdown> = () => {
  return (
    <Layout>
      <Dropdown>
        <Dropdown.Trigger>select</Dropdown.Trigger>
        <Dropdown.OptionList>
          <div>
            <li>
              <button>hel</button>
            </li>
            <li>
              <button>sel</button>
            </li>
          </div>
        </Dropdown.OptionList>
      </Dropdown>
    </Layout>
  );
};
export const DropdownTemplate: ComponentStory<typeof Dropdown> = Template.bind({});
