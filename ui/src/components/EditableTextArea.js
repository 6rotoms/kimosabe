import React, { useState } from 'react';
import PropTypes from 'prop-types';

import { Button, Flex } from './index';

const EditableTextArea = ({ initialText = '', onSave, isToggleable = false, ...props }) => {
  const [bio, setBio] = useState(initialText);
  const [unsavedBio, setUnsavedBio] = useState(bio);
  const [editable, setEditable] = useState(false);

  const handleSave = () => {
    const shouldSave = editable;
    setEditable(!editable);
    setBio(unsavedBio);
    if (shouldSave) {
      onSave(unsavedBio);
    }
  };

  const handleChange = (text) => {
    setUnsavedBio(text);
  };

  const handleCancel = () => {
    setEditable(!editable);
    setUnsavedBio(bio);
  };

  return (
    <Flex wrap={true} direction="flex-col" className="w-full" {...props}>
      {editable ? (
        <textarea
          data-testid="display-textarea"
          className={
            'flex-1 ' +
            'bg-grey-darkest ' +
            'px-px5 ' +
            'py-px10 ' +
            'h-inherit ' +
            'border-none ' +
            'rounded-md ' +
            'text-ivory ' +
            'resize-none ' +
            'break-words ' +
            'whitespace-pre-wrap'
          }
          onChange={(e) => handleChange(e.target.value)}
          value={unsavedBio}
        />
      ) : (
        <Flex
          data-testid="display-text"
          className="flex-1 overflow-hidden break-all whitespace-pre-line px-px5 py-px10 text-ivory"
        >
          {bio}
        </Flex>
      )}
      {isToggleable && (
        <Flex justify="justify-end" height="h-auto" data-testid="button-container">
          {editable && (
            <Button onClick={handleCancel} className="mr-0 m-2.5">
              Cancel
            </Button>
          )}
          <Button color="green" onClick={handleSave} className="mr-0 m-2.5">
            {editable ? 'Save' : 'Edit'}
          </Button>
        </Flex>
      )}
    </Flex>
  );
};

EditableTextArea.propTypes = {
  initialText: PropTypes.string,
  onSave: PropTypes.func.isRequired,
  isToggleable: PropTypes.bool,
};

export default EditableTextArea;
