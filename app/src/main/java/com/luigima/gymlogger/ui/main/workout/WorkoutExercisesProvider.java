package com.luigima.gymlogger.ui.main.workout;

/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


import android.support.v4.util.Pair;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.luigima.gymlogger.ui.base.advancedrecyclerview.AbstractExpandableDataProvider;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class WorkoutExercisesProvider extends AbstractExpandableDataProvider {
    private List<Pair<GroupData, List<ChildData>>> mData;

    // for undo group item
    private Pair<GroupData, List<ChildData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private ChildData mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    public WorkoutExercisesProvider(List<Pair<GroupData, List<ChildData>>> mData) {
        this.mData = mData;
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<ChildData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }

    @Override
    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final Pair<GroupData, List<ChildData>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        Timber.d("moved from (%d,%d) to (%d,%d)", fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
        final Pair<GroupData, List<ChildData>> fromGroup = mData.get(fromGroupPosition);
        final Pair<GroupData, List<ChildData>> toGroup = mData.get(toGroupPosition);

        final ConcreteChildData item = (ConcreteChildData) fromGroup.second.remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
            final long newId = ((ConcreteGroupData) toGroup.first).generateNewChildId();
            item.setChildId((int) newId);
        }

        toGroup.second.add(toChildPosition, item);
    }

    @Override
    public void removeGroupItem(int groupPosition) {
        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;

        mLastRemovedChild = null;
        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
    }

    @Override
    public void removeChildItem(int groupPosition, int childPosition) {
        mLastRemovedChild = mData.get(groupPosition).second.remove(childPosition);
        mLastRemovedChildParentGroupId = mData.get(groupPosition).first.getGroupId();
        mLastRemovedChildPosition = childPosition;

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }

    @Override
    public long addGroupItem(int splitId, String title) {
        final long groupId = getGroupCount();
        final WorkoutExercisesProvider.ConcreteGroupData group =
                new WorkoutExercisesProvider.ConcreteGroupData(groupId, splitId, title);
        final List<ChildData> children = new ArrayList<>();

        mData.add(new Pair<>(group, children));

        return groupId;
    }

    @Override
    public void setGroupItem(int groupPosition, String text) {
        //TODO mData.get(groupPosition).first
    }

    @Override
    public long addChildItem(int groupPosition, String text, int dataId) {
        int childId = mData.get(groupPosition).second.size();
        ChildData childData = new ConcreteChildData(childId, text, dataId);
        mData.get(groupPosition).second.add(childId, childData);

        return childId;
    }

    @Override
    public long setChildItem(int groupPosition, int childPosition, String text, int dataId) {
        mData.get(groupPosition).second.remove(childPosition);
        mData.get(groupPosition).second.add(childPosition, new ConcreteChildData(childPosition, text, dataId));

        return childPosition;
    }

    @Override
    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            return undoGroupRemoval();
        } else if (mLastRemovedChild != null) {
            return undoChildRemoval();
        } else {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }
    }

    private long undoGroupRemoval() {
        int insertedPosition;
        if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
            insertedPosition = mLastRemovedGroupPosition;
        } else {
            insertedPosition = mData.size();
        }

        mData.add(insertedPosition, mLastRemovedGroup);

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;

        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(insertedPosition);
    }

    private long undoChildRemoval() {
        Pair<GroupData, List<ChildData>> group = null;
        int groupPosition = -1;

        // find the group
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).first.getGroupId() == mLastRemovedChildParentGroupId) {
                group = mData.get(i);
                groupPosition = i;
                break;
            }
        }

        if (group == null) {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }

        int insertedPosition;
        if (mLastRemovedChildPosition >= 0 && mLastRemovedChildPosition < group.second.size()) {
            insertedPosition = mLastRemovedChildPosition;
        } else {
            insertedPosition = group.second.size();
        }

        group.second.add(insertedPosition, mLastRemovedChild);

        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
        mLastRemovedChild = null;

        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, insertedPosition);
    }

    @Override
    public void clear() {
        mData.clear();
    }

    public static final class ConcreteGroupData extends GroupData {

        private final long mId;
        private final String mText;
        private boolean mPinned;
        private long mNextChildId;
        private int setId;

        ConcreteGroupData(long id, int splitId, String text) {
            mId = id;
            mText = text;
            mNextChildId = 0;
            this.setId = splitId;
        }

        @Override
        public long getGroupId() {
            return mId;
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        @Override
        public int getDataId() {
            return setId;
        }

        public long generateNewChildId() {
            final long id = mNextChildId;
            mNextChildId += 1;
            return id;
        }
    }

    public static final class ConcreteChildData extends ChildData {

        private int id;
        private final String text;
        private boolean isPinned;
        private int dataId;

        ConcreteChildData(int id, String text, int dataId) {
            this.id = id;
            this.text = text;
            this.dataId = dataId;
        }

        @Override
        public long getChildId() {
            return id;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public void setPinned(boolean pinned) {
            isPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return isPinned;
        }

        public void setChildId(int id) {
            this.id = id;
        }

        public int getDataId() {
            return dataId;
        }
    }
}