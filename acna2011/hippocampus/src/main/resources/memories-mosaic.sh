#!/bin/bash

#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

MEMORIES_DIR=/x1/apachecon/memories

COLLECTION_DIR=${MEMORIES_DIR}/upload
OVERLAY_DIR=${MEMORIES_DIR}/overlay
OVERLAY=${1:-feather}
OVERLAY_FILE=${OVERLAY_DIR}/${OVERLAY}.jpg

if [ ! -d "${COLLECTION_DIR}" ]; then
    echo Collection directory \'${COLLECTION_DIR}\' should exist and contain your collection of jpg thumbnails
    exit 1
fi

if [ ! -d "${OVERLAY_DIR}" ]; then
    echo Overlay directory \'${OVERLAY_DIR}\' should exist and contain jpg files to overlay as mosaics
    exit 1
fi

if [ ! -f "${OVERLAY_FILE}" ]; then
    echo Overlay file \'${OVERLAY_FILE}\' not found.
    exit 1
fi

# usage: pymos [-h] [-z ZOOM] [-ts THUMBSIZE] [-f FUZZFACTOR] [-v] [-nc]
#             input output collection
pymos ${OVERLAY_FILE} ${MEMORIES_DIR}/mosaic.jpg ${COLLECTION_DIR}
