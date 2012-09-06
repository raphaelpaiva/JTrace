#!/bin/sh

mencoder mf://movingframe-*.png -mf fps=60:type=png -ovc x264 -x264encopts bitrate=10485760 -o output.avi

