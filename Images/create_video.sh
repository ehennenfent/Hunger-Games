for ((f=0; f<$1; f++)); do convert +append Timeline_Board/$f.png Leaderboard/$f.png Montage-P/$f.png; done
for ((f=0; f<$1; f++)); do convert -append Viewer/$f.png Montage-P/$f.png Montage/montage$f.png; done
cd Montage
ffmpeg -f image2 -r 7 -i montage%d.png -vcodec libx264 -crf 22 montage.mkv

