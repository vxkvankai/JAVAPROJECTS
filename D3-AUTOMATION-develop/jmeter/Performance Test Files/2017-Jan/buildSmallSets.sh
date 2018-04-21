#!/bin/bash
for i in {1..9}; do mkdir -p ./datasets/Test${i}; ./buildTCFLoad.sh -J accountOffset=${i}0000 -J groupOffset=${i}0 -J conduitFileDropPath ./datasets/Test${i}; done

